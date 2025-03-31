import { DataTypes } from "sequelize";
import { sequelize } from "../database.js";

const User = sequelize.define("User", {
    first_name: DataTypes.TEXT,
    last_name: DataTypes.TEXT,
    email: DataTypes.TEXT,
    password: DataTypes.TEXT
})

export default User