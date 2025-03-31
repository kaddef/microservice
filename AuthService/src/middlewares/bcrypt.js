import bcrypt from "bcrypt"

const saltRounds = 10;

export function hashPassword(plainPassword) {
    return new Promise((resolve, reject) => {
        bcrypt.hash(plainPassword, saltRounds, function(err, hash) {
            if(err) {
                reject(err);
                return;
            }

            resolve(hash);
        }) 
    })
}

export function compareHash(plainPassword, hashedPassword) {
    return new Promise((resolve, reject) => {
        bcrypt.compare(plainPassword, hashedPassword, function(err, result) {
            if(err) {
                reject(err)
                return
            }

            resolve(result)
        });
    })
}