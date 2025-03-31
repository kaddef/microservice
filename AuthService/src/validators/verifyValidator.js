import Joi from "joi";

const verifySchema = Joi.object({
    token: Joi.string()
        .pattern(/^[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+$/)
        .required(),

    includeUser: Joi.bool()
        .required()
})

function validateVerify(req, res, next) {
    const { error } = verifySchema.validate(req.body, { abortEarly: false })
    if(error) {
        console.log(error.details[0])
        return res.status(400).json({
            valid: false,
            message: "Validation failed",
            badFields: error.details.map(detail => detail.context.key)
        });
    }
    next()
}

export default validateVerify