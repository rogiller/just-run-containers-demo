package com.fargate.fargatedemo

import org.apache.commons.lang3.time.DurationFormatUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import java.lang.management.ManagementFactory

@RestController
@RequestMapping("/")
class FargateController {

    static final String UPTIME_DURATION_FORMAT = "d' day, 'H' hour, 'm' min, 's' sec'"

    int jokeRequestCount = 0

    @GetMapping("")
    String fargateDemo(){
        String chuck = '<a href="/chuck">/chuck</a>'
        return """<p>HEY there random internet person... you've stumbled upon Roger's Fargate Demo!
                    Hit $chuck for a random Chuck Norris joke. I am not responsible for cleanness of the jokes. :)</p>
               """
    }

    @GetMapping("/chuck")
    Map chuckNorrisJoke(){

        jokeRequestCount++

        def jokeJson = new RestTemplate().getForObject('http://api.icndb.com/jokes/random', Object)

        Map mapResult = [:]

        //noinspection GrUnresolvedAccess
        mapResult.joke = jokeJson?.value?.joke as String
        mapResult.requestNumber = jokeRequestCount
        mapResult.jvmId = ManagementFactory.getRuntimeMXBean().getName()
        mapResult.jvmVersion = ManagementFactory.getRuntimeMXBean().getVmVersion()
        mapResult.jvmVendor = ManagementFactory.getRuntimeMXBean().getVmVendor()
        mapResult.jvmUptime = getUptimeString(ManagementFactory.getRuntimeMXBean().getUptime())

        return mapResult
    }

    static String getUptimeString(long uptime) {
        return DurationFormatUtils.formatDuration(uptime, UPTIME_DURATION_FORMAT)
    }
}
