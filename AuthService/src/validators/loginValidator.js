import Joi from "joi";

const loginSchema = Joi.object({
    email: Joi.string()
        .email()
        .required(),

    password: Joi.string()
        .min(6)
        .required()
})

function validateLogin(req, res, next) {
    const { error } = loginSchema.validate(req.body)
    if(error) {
        return res.status(400).send(error.details[0].message);
    }
    next();
}

export default validateLogin