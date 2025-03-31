import express from "express";
import validateLogin from "../validators/loginValidator.js";
import userService from "../services/userService.js";
import validateRegister from "../validators/registerValidator.js";

const router = express.Router();

router.post("/login", validateLogin, async (req, res) => {
    const { email, password } = req.body
    const result = await userService.login(email, password);
    console.log(result)
    if (!result.success) {
        if (result.error === "USER_NOT_FOUND") {
            return res.status(404).json({
                success: false,
                message: "No account found with this email address"
            });
        } else if (result.error === "INVALID_PASSWORD") {
            return res.status(401).json({
                success: false,
                message: "Incorrect password"
            });
        }
    }

    res.status(200).json({
        success: true,
        token: result.token
    });
})

router.post("/register", validateRegister, async(req, res) => {
    const result = await userService.register(req.body);

    if (!result.success) {
        if (result.error === "EMAIL_ALREADY_EXISTS") {
            return res.status(409).json({
                success: false,
                message: "Email already exists"
            }); // Conflict
        } else if (result.error === "VALIDATION_ERROR") {
            return res.status(400).json({
                success: false,
                message: "Database validation error"
            }); // Bad Request
        } else {
            return res.status(500).json({
                success: false,
                message: "Could not complete registration"
            }); // Server Error
        }
    }

    // Success case
    return res.status(201).json(result); // Created
})

router.post("/logout", (req, res) => {
    res.send("logout")
})

router.post("/refresh", (req, res) => {
    res.send("refresh")
})


export default router