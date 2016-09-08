/*
 * Copyright (c) 2016 Schibsted Media Group. All rights reserved
 */
package io.github.lvicentesanchez.nomdb

import com.twitter.util.{ Future => TwitterFuture, Promise => TwitterPromise }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Future => ScalaFuture }
import scala.util.{ Failure, Success }

package object utils {

  implicit class ScalaFutureToTwitter[A](val future: ScalaFuture[A]) extends AnyVal {
    def asTwitter: TwitterFuture[A] = {
      val p = new TwitterPromise[A]()
      future.onComplete {
        case Success(value)     => p.setValue(value)
        case Failure(exception) => p.setException(exception)
      }
      p
    }
  }
}
