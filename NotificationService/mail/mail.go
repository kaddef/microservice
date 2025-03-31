package mail

import (
	"NotificationService/utils"
	"fmt"

	gomail "gopkg.in/mail.v2"
)

type EmailSender struct {
	sender      string
	appPassword string // Google app passwords
}

func NewEmailSender() *EmailSender {
	return &EmailSender{
		// sender:      "erkanerdil016@gmail.com",
		// appPassword: "geix nrml yhka vcuk",
		sender:      utils.GoGetEnvVariable("SERVER_MAIL"),
		appPassword: utils.GoGetEnvVariable("APP_PASSWORD"),
	}
}

func (es *EmailSender) SendMail(to, subject, body string) bool {
	message := gomail.NewMessage()

	message.SetHeader("From", es.sender)
	message.SetHeader("To", to)
	message.SetHeader("Subject", subject)

	message.SetBody("text/plain", body)
	//message.AddAlternative() An Alternative if i send an html body and client isnt supports html

	dialer := gomail.NewDialer("smtp.gmail.com", 587, es.sender, es.appPassword)

	if err := dialer.DialAndSend(message); err != nil {
		fmt.Println("Error:", err)
		return false
	} else {
		fmt.Println("Email sent successfully!")
		return true
	}
}
