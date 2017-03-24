package edu.umass.cs.automan.adapter.mturk.logging

import java.util.UUID

import edu.umass.cs.automan.adapters.mturk.DSL._
import edu.umass.cs.automan.adapters.mturk.mock.MockSetup
import edu.umass.cs.automan.core.answer.Answer
import edu.umass.cs.automan.core.logging.LogLevelDebug
import org.scalatest.tagobjects.Slow
import edu.umass.cs.automan.test._
import org.scalatest.{FlatSpec, Matchers}
import scala.util.Random
import org.scalatest.Ignore

@Ignore
class MTurkManyThreadsTest extends FlatSpec with Matchers {

  "A freetext program with many threads" should "work" taggedAs Slow in {
    val urls = Random.alphanumeric.take(1000).mkString.grouped(10).toList

    val a = MTurkAdapter { mt =>
      mt.access_key_id = UUID.randomUUID().toString
      mt.secret_access_key = UUID.randomUUID().toString
      mt.use_mock = MockSetup(budget = 8.00)
      mt.logging = LogConfig.TRACE_MEMOIZE_VERBOSE
      mt.log_verbosity = LogLevelDebug()
    }

    def plateTxt(url: String) = a.FreeTextQuestion { q =>
      q.budget = 5.00
      q.title = "Transcribe these license plates."
      q.text = "Foobitty foobitty foo? http://" + url
      q.allow_empty_pattern = true
      q.pattern = "XXXXXXXXX"
      q.dont_reject = true
      q.mock_answers = makeMocks(Utilities.randomPermute(List(url, url, url + "z", url)))
    }

    automan(a, test_mode = true, in_mem_db = true) {
      // get plate texts from image URLs
      val plate_texts = urls.map { url =>
        (url, plateTxt(url))
      }

      // print out results
      plate_texts.foreach { case (url, outcome) =>
        outcome.answer match {
          case Answer(ans, _, _, _, _) =>
            println(url + ": " + ans)
            ans should be (url)
          case _ => fail()
        }
      }
    }
  }
}
