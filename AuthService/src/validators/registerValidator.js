import Joi from "joi";

const registerSchema = Joi.object({
    first_name: Joi.string()
        .required(),

    last_name: Joi.string()
        .required(),

    email: Joi.string()
        .email()
        .required(),

    password: Joi.string()
        .min(6)
        .required()
})

function validateRegister(req, res, next) {
    const { error } = registerSchema.validate(req.body)
    if(error) {
        return res.status(400).send(error.details[0].message);
    }
    next();
}

export default validateRegister