FROM golang:1.24-alpine AS builder

WORKDIR /app

COPY . .

RUN go mod tidy && go build -o mail-service

FROM alpine:latest

WORKDIR /root/

COPY --from=builder /app/mail-service .

EXPOSE 8001

ENV RMQP_URL=amqp://guest:guest@localhost:5672/
ENV QUEUE_NAME=Testo
ENV SERVER_MAIL=erkanerdil016@gmail.com
ENV APP_PASSWORD="geix nrml yhka vcuk"

CMD ["./mail-service"]