import mill._
import mill.define.Sources
import mill.modules.Util
import scalalib._
import mill.bsp._
import scala.languageFeature.reflectiveCalls

object playing extends ScalaModule {
  def millSourcePath = os.pwd
  def scalaVersion = "3.3.1"
  def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature"
  )
}
