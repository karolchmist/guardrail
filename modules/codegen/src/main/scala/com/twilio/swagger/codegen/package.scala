package com.twilio.swagger

import cats.data.Coproduct
import cats.syntax.either._
import com.twilio.swagger.codegen.terms.ScalaTerm
import com.twilio.swagger.codegen.terms.client.ClientTerm
import com.twilio.swagger.codegen.terms.protocol.{AliasProtocolTerm, EnumProtocolTerm, ModelProtocolTerm, ProtocolSupportTerm}
import com.twilio.swagger.codegen.terms.server.ServerTerm
import scala.collection.immutable.Seq
import scala.meta._

package codegen {
  case class CodegenDefinitions(clients: Seq[Client], servers: Seq[Server], frameworkImports: Seq[Import])

  object Target {
    def pure[T](x: T): Target[T] = Either.right(x)
    def log[T](x: String): Target[T] = Either.left(x)
    def fromOption[T](x: Option[T], default: => String): Target[T] = Either.fromOption(x, default)
  }

  object CoreTarget {
    def pure[T](x: T): CoreTarget[T] = Either.right(x)
    def fromOption[T](x: Option[T], default: => Error): CoreTarget[T] = Either.fromOption(x, default)
    def log[T](x: Error): CoreTarget[T] = Either.left(x)
  }
}

package object codegen {
  type CodegenApplicationSP[T] = Coproduct[ProtocolSupportTerm, ServerTerm, T]
  type CodegenApplicationMSP[T] = Coproduct[ModelProtocolTerm, CodegenApplicationSP, T]
  type CodegenApplicationEMSP[T] = Coproduct[EnumProtocolTerm, CodegenApplicationMSP, T]
  type CodegenApplicationCEMSP[T] = Coproduct[ClientTerm, CodegenApplicationEMSP, T]
  type CodegenApplicationACEMSP[T] = Coproduct[AliasProtocolTerm, CodegenApplicationCEMSP, T]
  type CodegenApplicationACEMSSP[T] = Coproduct[ScalaTerm, CodegenApplicationACEMSP, T]
  type CodegenApplication[T] = CodegenApplicationACEMSSP[T]

  type Target[A] = Either[String, A]
  type CoreTarget[A] = Either[Error, A]
}