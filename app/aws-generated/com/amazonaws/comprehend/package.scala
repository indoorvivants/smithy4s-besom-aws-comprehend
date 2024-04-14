package com.amazonaws

package object comprehend {
  type Comprehend[F[_]] = smithy4s.kinds.FunctorAlgebra[ComprehendGen, F]
  val Comprehend = ComprehendGen

  type CustomerInputString = com.amazonaws.comprehend.CustomerInputString.Type

}