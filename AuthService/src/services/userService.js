import { compareHash, hashPassword } from "../middlewares/bcrypt.js";
import { generateToken } from "../middlewares/jwtMiddleware.js";
import User from "../models/User.js";

const userService = {
    async login(email, password) {
        const user = await User.findOne({where: {email: email}})

        if(!user) {
            return { success: false, error: "USER_NOT_FOUND" };
        }
        if(!await compareHash(password, user.password)) {
            return { success: false, error: "INVALID_PASSWORD" };
        }
        const token = generateToken(email);
        return { success: true, token: token };
    },

    async register(userData) {
        try {
            const existingUser = await User.findOne({ where: { email: userData.email } });
    
            if(existingUser) {
                return { success: false, error: "EMAIL_ALREADY_EXISTS" };
            }

            userData.password = await hashPassword(userData.password);
    
            const user = await User.create(userData);
    
            const token = generateToken(userData.email);
    
            return { 
                success: true, 
                user: {
                    id: user.id,
                    email: user.email,
                },
                token: token
            };
        } catch (error) {
            if (error.name === 'SequelizeValidationError') {
                return { 
                    success: false, 
                    error: "DATABASE_VALIDATION_ERROR",
                    message: "Invalid user data",
                    details: error.errors.map(err => ({ field: err.path, message: err.message }))
                };
            }

            return { 
                success: false, 
                error: "REGISTRATION_FAILED",s
            };
        }
    },

    async getUserFromId(userId) {
        try {
            const user = await User.findOne({ where: { id: userId } });
            return { 
                success: true, 
                userData: user.dataValues,
            };

        } catch (error) {
            return { 
                success: false, 
            };
        }
    },

    async getUserFromEmail(email) {
        try {
            const user = await User.findOne({ where: { email: email } });
            return { 
                success: true, 
                userData: user.dataValues,
            };
        } catch (error) {
            return { 
                success: false, 
            };
        }
    }
};

export default userService;