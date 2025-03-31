package utils

import (
	"os"

	"github.com/joho/godotenv"
)

func InitEnv() {
	err := godotenv.Load(".env")
	if err != nil {
		panic("Error loading .env file")
	}
}

func GoGetEnvVariable(key string) string {
	return os.Getenv(key)
}
