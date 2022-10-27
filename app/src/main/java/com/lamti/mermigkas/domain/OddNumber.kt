package com.lamti.mermigkas.domain

class OddNumber private constructor(n: Int) {

    companion object {

        fun of(n: Int): Either<EvenNumberError, OddNumber> =
            if (n.mod(2) == 0) Either.Error(EvenNumberError)
            else Either.Success(OddNumber(n))
    }
}
