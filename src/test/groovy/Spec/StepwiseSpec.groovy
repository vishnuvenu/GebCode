package Spec

/**
 * Created by mehmetgerceker on 11/17/15.
 */
import Pages.BingResultPage
import Pages.YahooQueryPage
import Pages.YahooResultPage
import geb.driver.CachingDriverFactory
import spock.lang.Stepwise

@Stepwise
class StepwiseSpec extends SauceSpecBase {
    /*
     *  Since you are taking over the driver.quit()
     *  you need to make sure you quit the driver after using it.
     *  in this instance it is the end of the spec.
    */
    @Override
    def cleanupSpec() {
        if(driver){
            CachingDriverFactory.clearCache()
            driver.quit()
        }

    }

    def "Search \"hello!\""() {
        String q = "hello!"
        YahooResultPage.searchWord = q

        when:
        to YahooQueryPage

        and:
        search(q)

        then:
        waitFor {at YahooResultPage}
    }

    def "Search blank"() {
        String q = "hello!"
        YahooResultPage.searchWord = q

        when:
        to YahooQueryPage

        and:
        search(q)

        then:
        waitFor {at YahooResultPage}
    }

}
