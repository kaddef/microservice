package main

import (
	"NotificationService/mail"
	"NotificationService/queue"
	"NotificationService/utils"
	"encoding/json"
	"fmt"
)

type MessageContent struct {
	From    string `json:"from"` // Server Mail
	To      string `json:"to"`
	Subject string `json:"subject"`
	Body    string `json:"body"`
}

func main() {
	utils.InitEnv()
	emailSender := mail.NewEmailSender()

	handler := func(message []byte) error {
		var content MessageContent

		err := json.Unmarshal(message, &content)
		if err != nil {
			fmt.Println("Error while parsing JSON")
		}

		fmt.Println("HERE")
		fmt.Printf("Received message: %s\n", string(message))
		fmt.Println(content)
		emailSender.SendMail(content.To, content.Subject, content.Body)
		// Add your message processing logic here
		return nil
	}

	//CONSUMER INIT
	// consumer, err := queue.NewRMQConsumer("amqp://guest:guest@localhost:5672/", "Testo", handler)
	consumer, err := queue.NewRMQConsumer(utils.GoGetEnvVariable("RMQP_URL"), utils.GoGetEnvVariable("QUEUE_NAME"), handler)
	if err != nil {
		panic(err)
	}
	defer consumer.Close()
	//

	err = consumer.StartConsuming()
	if err != nil {
		panic(err)
	}

	select {}
}
