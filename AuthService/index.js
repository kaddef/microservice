import express from "express"
import bodyParser from "body-parser";
import { sequelize } from "./src/database.js"
import Routes from "./src/api/index.js"
import "dotenv/config"

const app = express();
const PORT = 8000

app.use(bodyParser.json())
app.use(Routes)

async function initializeApp() {
    try {
        await sequelize.authenticate();
        console.log('Database connection established.');
        
        // Sync database models
        await sequelize.sync();
        console.log('Database models synchronized.');

        app.listen(PORT, () => {
            console.log(`Server is running on port ${PORT}`);
        });
    } catch (error) {
        console.error('Unable to connect to the database:', error);
    }
}

initializeApp();