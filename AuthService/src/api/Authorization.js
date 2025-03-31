import express from "express";
import authenticate from "../middlewares/auth.js";
import validateVerify from "../validators/verifyValidator.js";
import extractToken from "../utils/extractToken.js";
import { verifyToken } from "../middlewares/jwtMiddleware.js";
import jwt from "jsonwebtoken";
import userService from "../services/userService.js";


const router = express.Router();

router.post("/verify", validateVerify, async (req, res) => {
    try {
        const jwtSecretKey = process.env.JWT_SECRET_KEY;
        const { token, includeUser } = req.body;
        const decoded = jwt.verify(token, jwtSecretKey);
        var paylod = { valid: true }
        
        if (includeUser) {
            const result = await userService.getUserFromEmail(decoded);
            if (!result.success) {
                return res.status(200).json({ valid: false, error: "User not found" }); //400
            }
            const {id, password, createdAt, updatedAt, ...rest} = result.userData;
            paylod.userData = rest
        }

        res.status(200).json(paylod)
    } catch (error) {
        res.status(200).json({ valid: false, error: "Invalid or expired token" }); // 401
    }
});

export default router