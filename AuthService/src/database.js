import { Sequelize } from "sequelize";

console.log("DENEME")
console.log(process.env.DB_NAME)
console.log(process.env.DB_USER)
console.log(process.env.DB_HOST)
console.log(process.env.DB_PORT)
export const sequelize = new Sequelize(
    process.env.DB_NAME || "auth_service",
    process.env.DB_USER || "root",
    process.env.DB_PASSWORD || "kaddef",
    {
        host: process.env.DB_HOST || "localhost",
        port: process.env.DB_PORT || 3306,
        dialect: "mysql"
    }
);

async function testConnection(){
    try {
        await sequelize.authenticate();
        console.log('Connection has been established successfully.');
    } catch (error) {
        console.error('Unable to connect to the database:', error);
    }
}

testConnection()