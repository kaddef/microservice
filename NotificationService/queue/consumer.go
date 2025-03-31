package queue

import (
	"fmt"

	amqp "github.com/rabbitmq/amqp091-go"
)

type MessageHandler func(message []byte) error

type RMQConsumer struct {
	connection *amqp.Connection
	channel    *amqp.Channel
	queueURL   string
	queueName  string
	handler    MessageHandler
}

func NewRMQConsumer(queueURL, queueName string, handler MessageHandler) (*RMQConsumer, error) {
	consumer := &RMQConsumer{
		queueURL:  queueURL,
		queueName: queueName,
		handler:   handler,
	}

	err := consumer.connect()
	if err != nil {
		return nil, err
	}

	return consumer, nil
}

func (c *RMQConsumer) connect() error {
	var err error
	c.connection, err = amqp.Dial(c.queueURL)
	if err != nil {
		return fmt.Errorf("failed to connect to RabbitMQ: %w", err)
	}

	c.channel, err = c.connection.Channel()
	if err != nil {
		return fmt.Errorf("failed to open channel: %w", err)
	}

	_, err = c.channel.QueueDeclare(
		c.queueName,
		false,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		return fmt.Errorf("failed to declare queue: %w", err)
	}

	fmt.Println("Successfully connected to RabbitMQ")
	return nil
}

func (c *RMQConsumer) StartConsuming() error {
	messages, err := c.channel.Consume(
		c.queueName, // queue name
		"",          // consumer tag - empty for auto-generated
		false,       // auto acknowledge
		false,       // exclusive
		false,       // no local
		false,       // no wait
		nil,         // arguments
	)
	if err != nil {
		return fmt.Errorf("failed to register consumer: %w", err)
	}

	go func() {
		for d := range messages {
			err := c.handler(d.Body)
			if err != nil {
				fmt.Printf("Error processing message: %v", err)
				// Negative acknowledgement on error
				_ = d.Nack(false, true) // requeue the message
			} else {
				// Positive acknowledgement on successful processing
				_ = d.Ack(false)
			}
		}
	}()

	select {}
}

func (c *RMQConsumer) Close() {
	if c.channel != nil {
		c.channel.Close()
	}
	if c.connection != nil {
		c.connection.Close()
	}
}
