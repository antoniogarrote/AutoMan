package edu.umass.cs.automan.adapter.mturk

import edu.umass.cs.automan.core.logging.LogLevelDebug
import org.scalatest._
import java.util.UUID
import edu.umass.cs.automan.test._
import edu.umass.cs.automan.adapters.mturk.DSL._
import edu.umass.cs.automan.adapters.mturk.mock.MockSetup

class MTurkMultiHITTest extends FlatSpec with Matchers {

  "A radio button program with disagreeing answers" should "spawn multiple HITs." in {
    val confidence = 0.95

    val a = MTurkAdapter { mt =>
      mt.access_key_id = UUID.randomUUID().toString
      mt.secret_access_key = UUID.randomUUID().toString
      mt.use_mock = MockSetup(budget = 8.00)
      mt.logging = LogConfig.NO_LOGGING
      mt.log_verbosity = LogLevelDebug()
    }

    def which_one() = a.RadioButtonQuestion { q =>
      q.confidence = confidence
      q.budget = 8.00
      q.initial_worker_timeout_in_s = 30
      q.question_timeout_multiplier = 1
      q.text = "Which one of these does not belong?"
      q.options = List(
        a.Option('oscar, "Oscar the Grouch"),
        a.Option('kermit, "Kermit the Frog"),
        a.Option('spongebob, "Spongebob Squarepants"),
        a.Option('cookie, "Cookie Monster"),
        a.Option('count, "The Count")
      )
      q.mock_answers =
        makeMocksAt(List('spongebob, 'kermit, 'spongebob), 0) :::
          makeMocksAt(List('spongebob, 'spongebob, 'spongebob), 45000)
    }

    automan(a, test_mode = true) {
      which_one().answer match {
        case Answer(value, cost, conf, _, _) =>
          println("Answer: '" + value + "', confidence: " + conf + ", cost: $" + cost + ", # HITs: " + a.getAllHITs.length)
          (value == 'spongebob) should be (true)
          (conf >= confidence) should be (true)
          (cost == BigDecimal(0.48)) should be (true)
          a.getAllHITs.length should be (2)
        case LowConfidenceAnswer(value, cost, conf, _, _) =>
          fail()
      }
    }
  }
}
