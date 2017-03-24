package edu.umass.cs.automan.core

import edu.umass.cs.automan.core.answer.{EstimationOutcome, ScalarOutcome, VectorOutcome}
import edu.umass.cs.automan.core.mock.MockAnswer
import edu.umass.cs.automan.core.question._
import edu.umass.cs.automan.core.question.confidence.ConfidenceInterval

trait DSL {
  // to simplify imports
  val automan = edu.umass.cs.automan.automan
  val Answer = edu.umass.cs.automan.core.answer.Answer
  val Answers = edu.umass.cs.automan.core.answer.Answers
  val AsymmetricCI = edu.umass.cs.automan.core.question.confidence.AsymmetricCI
  val DistributionOutcome = edu.umass.cs.automan.core.answer.VectorOutcome
  val OverBudgetAnswer = edu.umass.cs.automan.core.answer.OverBudgetAnswer
  val OverBudgetAnswers = edu.umass.cs.automan.core.answer.OverBudgetAnswers
  val LogConfig = edu.umass.cs.automan.core.logging.LogConfig
  val LowConfidenceAnswer = edu.umass.cs.automan.core.answer.LowConfidenceAnswer
  val IncompleteAnswers = edu.umass.cs.automan.core.answer.IncompleteAnswers
  val ScalarOutcome = edu.umass.cs.automan.core.answer.ScalarOutcome
  val SymmetricCI = edu.umass.cs.automan.core.question.confidence.SymmetricCI
  val UnconstrainedCI = edu.umass.cs.automan.core.question.confidence.UnconstrainedCI
  val Utilities = edu.umass.cs.automan.core.util.Utilities

  // DSL constructs
  def estimate[A <: AutomanAdapter](
               confidence_interval: ConfidenceInterval,
               confidence: Double = MagicNumbers.DefaultConfidence,
               budget: BigDecimal = MagicNumbers.DefaultBudget,
               default_sample_size: Int = -1,
               dont_reject: Boolean = true,
               dry_run: Boolean = false,
               estimator: Seq[Double] => Double = null,
               image_alt_text: String = null,
               image_url: String = null,
               max_value: Double = Double.MaxValue,
               min_value: Double = Double.MinValue,
               mock_answers: Iterable[MockAnswer[Double]] = null,
               pay_all_on_failure: Boolean = true,
               text: String,
               title: String = null,
               wage: BigDecimal = MagicNumbers.USFederalMinimumWage
              )
              (implicit a: A) : EstimationOutcome = {
    def initf[Q <: EstimationQuestion](q: Q) = {
      // mandatory parameters
      q.confidence_interval = confidence_interval
      q.text = text

      // mandatory parameters with sane defaults
      q.confidence = confidence
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (default_sample_size != -1 && default_sample_size > 0) { q.default_sample_size = default_sample_size }
      if (estimator != null) { q.estimator = estimator }
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (max_value != Double.MaxValue) { q.max_value = max_value }
      if (min_value != Double.MinValue) { q.min_value = min_value }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.EstimationQuestion(initf)
  }

  def freetext[A <: AutomanAdapter](
                        confidence: Double = MagicNumbers.DefaultConfidence,
                        budget: BigDecimal = MagicNumbers.DefaultBudget,
                        dont_reject: Boolean = true,
                        dry_run: Boolean = false,
                        image_alt_text: String = null,
                        image_url: String = null,
                        mock_answers: Iterable[MockAnswer[String]] = null,
                        pay_all_on_failure: Boolean = true,
                        pattern: String,
                        pattern_error_text: String = null,
                        text: String,
                        title: String = null,
                        wage: BigDecimal = MagicNumbers.USFederalMinimumWage
                       )
                       (implicit a: A) : ScalarOutcome[String] = {
    def initf[Q <: FreeTextQuestion](q: Q) = {
      // mandatory parameters
      q.text = text
      q.pattern = pattern

      // mandatory parameters with sane defaults
      q.confidence = confidence
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (pattern_error_text != null) { q.pattern_error_text = pattern_error_text }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.FreeTextQuestion(initf)
  }

  def freetexts[A <: AutomanAdapter](
                       sample_size: Int = MagicNumbers.DefaultSampleSizeForDistrib,
                       budget: BigDecimal = MagicNumbers.DefaultBudget,
                       dont_reject: Boolean = true,
                       dry_run: Boolean = false,
                       image_alt_text: String = null,
                       image_url: String = null,
                       mock_answers: Iterable[MockAnswer[String]] = null,
                       pay_all_on_failure: Boolean = true,
                       pattern: String,
                       pattern_error_text: String = null,
                       text: String,
                       title: String = null,
                       wage: BigDecimal = MagicNumbers.USFederalMinimumWage
                     )
                     (implicit a: A) : VectorOutcome[String] = {
    def initf[Q <: FreeTextVectorQuestion](q: Q) = {
      // mandatory parameters
      q.text = text
      q.pattern = pattern

      // mandatory parameters with sane defaults
      q.sample_size = sample_size
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (pattern_error_text != null) { q.pattern_error_text = pattern_error_text }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.FreeTextDistributionQuestion(initf)
  }

  def checkbox[A <: AutomanAdapter, O](
                       confidence: Double = MagicNumbers.DefaultConfidence,
                       budget: BigDecimal = MagicNumbers.DefaultBudget,
                       dont_reject: Boolean = true,
                       dry_run: Boolean = false,
                       image_alt_text: String = null,
                       image_url: String = null,
                       mock_answers: Iterable[MockAnswer[Set[Symbol]]] = null,
                       options: List[AnyRef],
                       pay_all_on_failure: Boolean = true,
                       text: String,
                       title: String = null,
                       wage: BigDecimal = MagicNumbers.USFederalMinimumWage
                     )
                     (implicit a: A) : ScalarOutcome[Set[Symbol]] = {
    def initf[Q <: CheckboxQuestion](q: Q) = {
      // mandatory parameters
      q.text = text
      q.options = options.asInstanceOf[List[q.QuestionOptionType]]  // yeah... ugly

      // mandatory parameters with sane defaults
      q.confidence = confidence
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.CheckboxQuestion(initf)
  }

  def checkboxes[A <: AutomanAdapter, O](
                      sample_size: Int = MagicNumbers.DefaultSampleSizeForDistrib,
                      budget: BigDecimal = MagicNumbers.DefaultBudget,
                      dont_reject: Boolean = true,
                      dry_run: Boolean = false,
                      image_alt_text: String = null,
                      image_url: String = null,
                      mock_answers: Iterable[MockAnswer[Set[Symbol]]] = null,
                      options: List[AnyRef],
                      pay_all_on_failure: Boolean = true,
                      text: String,
                      title: String = null,
                      wage: BigDecimal = MagicNumbers.USFederalMinimumWage
                    )
                    (implicit a: A) : VectorOutcome[Set[Symbol]] = {
    def initf[Q <: CheckboxVectorQuestion](q: Q) = {
      // mandatory parameters
      q.text = text
      q.options = options.asInstanceOf[List[q.QuestionOptionType]]  // yeah... ugly

      // mandatory parameters with sane defaults
      q.sample_size = sample_size
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.CheckboxDistributionQuestion(initf)
  }

  def radio[A <: AutomanAdapter, O](
                          confidence: Double = MagicNumbers.DefaultConfidence,
                          budget: BigDecimal = MagicNumbers.DefaultBudget,
                          dont_reject: Boolean = true,
                          dry_run: Boolean = false,
                          image_alt_text: String = null,
                          image_url: String = null,
                          mock_answers: Iterable[MockAnswer[Symbol]] = null,
                          options: List[AnyRef],
                          pay_all_on_failure: Boolean = true,
                          text: String,
                          title: String = null,
                          wage: BigDecimal = MagicNumbers.USFederalMinimumWage
                        )
                        (implicit a: A) : ScalarOutcome[Symbol] = {
    def initf[Q <: RadioButtonQuestion](q: Q) = {
      // mandatory parameters
      q.text = text
      q.options = options.asInstanceOf[List[q.QuestionOptionType]]  // yeah... ugly

      // mandatory parameters with sane defaults
      q.confidence = confidence
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.RadioButtonQuestion(initf)
  }

  def radios[A <: AutomanAdapter, O](
                           sample_size: Int = MagicNumbers.DefaultSampleSizeForDistrib,
                           budget: BigDecimal = MagicNumbers.DefaultBudget,
                           dont_reject: Boolean = true,
                           dry_run: Boolean = false,
                           image_alt_text: String = null,
                           image_url: String = null,
                           mock_answers: Iterable[MockAnswer[Symbol]] = null,
                           options: List[AnyRef],
                           pay_all_on_failure: Boolean = true,
                           text: String,
                           title: String = null,
                           wage: BigDecimal = MagicNumbers.USFederalMinimumWage
                         )
                         (implicit a: A) : VectorOutcome[Symbol] = {
    def initf[Q <: RadioButtonVectorQuestion](q: Q) = {
      // mandatory parameters
      q.text = text
      q.options = options.asInstanceOf[List[q.QuestionOptionType]]  // yeah... ugly

      // mandatory parameters with sane defaults
      q.sample_size = sample_size
      q.budget = budget
      q.dont_reject = dont_reject
      q.dry_run = dry_run
      q.pay_all_on_failure = pay_all_on_failure

      // optional parameters
      if (image_alt_text != null) { q.image_alt_text = image_alt_text }
      if (image_url != null) { q.image_url = image_url }
      if (title != null) { q.title = title }
      if (mock_answers != null ) { q.mock_answers = mock_answers }
    }
    a.RadioButtonDistributionQuestion(initf)
  }
}
