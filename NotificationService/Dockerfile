FROM golang:1.24-alpine AS builder

WORKDIR /app

COPY . .

RUN go mod tidy && go build -o mail-service

FROM alpine:latest

WORKDIR /root/

COPY --from=builder /app/mail-service .

COPY --from=builder /app/.env .

EXPOSE 8001

CMD ["./mail-service"]