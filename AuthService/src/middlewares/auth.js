import extractToken from "../utils/extractToken.js";
import { verifyToken } from "./jwtMiddleware.js";

export default function authenticate(req, res, next) {
    const authHeader = req.headers['authorization'];

    const token = extractToken(authHeader)

    if (!token) {
        return res.status(401).json({
            success: false,
            message: "Access denied. No token provided."
        });
    }

    const result = verifyToken(token)

    if (!result) {
        return res.status(403).json({
            success: false,
            message: "Invalid token."
        });
    }

    next();
}