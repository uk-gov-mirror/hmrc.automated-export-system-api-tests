/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.api.specs

import org.scalatest.BeforeAndAfterAll
import uk.gov.hmrc.api.helpers.PayloadLoader

class SubmitMessageSpec extends BaseSpec with BeforeAndAfterAll {

  private var bearerToken: String = _

  override def beforeAll(): Unit =
    bearerToken = service.getBearerToken.futureValue

  Feature("Submit IE507 Message") {

    Scenario("Valid XML submission returns 202") {

      Given("a valid IE507 XML payload and a valid bearer token")

      val xml =
        PayloadLoader.load("valid-ie507.xml")

      When("the payload is submitted to the AES message endpoint")

      val response =
        service
          .submitMessage(
            xml,
            bearerToken
          )
          .futureValue

      Then("the request is accepted")

      response.status shouldBe 202
    }

    Scenario("Invalid XML submission returns 400") {

      Given("an invalid IE507 XML payload and a valid bearer token")

      val xml =
        PayloadLoader.load("invalid-ie507.xml")

      When("the payload is submitted to the AES message endpoint")

      val response =
        service
          .submitMessage(
            xml,
            bearerToken
          )
          .futureValue

      Then("a bad request response is returned")

      response.status shouldBe 400
    }

    Scenario("Submission without token returns 401") {

      Given("a valid IE507 XML payload")

      val xml =
        PayloadLoader.load("valid-ie507.xml")

      When("the payload is submitted without authentication")

      val response =
        service
          .submitMessageWithoutAuth(xml)
          .futureValue

      Then("the request is rejected as unauthorised")

      response.status shouldBe 401
    }
  }
}
