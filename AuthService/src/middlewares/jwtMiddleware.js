import jwt from "jsonwebtoken";


export function generateToken(payload) {
    const jwtSecretKey = process.env.JWT_SECRET_KEY;
    
    const token = jwt.sign(payload, jwtSecretKey)

    return token
}

export function verifyToken(token) {
    try {
        const jwtSecretKey = process.env.JWT_SECRET_KEY;
    
        const verified = jwt.verify(token, jwtSecretKey);
    
        if(verified) {
            return true;
        } else {
            return false;
        }
    } catch (error) {
        console.error("Token verification error:", error);
        return false;
    }
}