#Details

The basic usage of the Simple Astronomy Lib goes like this:
```java
   MoonPhaseFinder.findFullMoonFollowing(cal)
```
Most methods in the API will return a `java.util.Date`

#Example

1. Download the latest Simple Astronomy Lib jar file from the [Downloads page](https://github.com/dustmachine/simple-astronomy-lib/releases).
1. Create a file `WerewolfCheck.java` with the following contents:
```java
import java.util.Calendar;
import java.util.TimeZone;
import com.bradsbrain.simpleastronomy.MoonPhaseFinder;

public class WerewolfCheck {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        cal.clear();
        cal.set(2011, Calendar.JUNE, 12);

        System.out.println("The next full moon is on: " + MoonPhaseFinder.findFullMoonFollowing(cal));
    }
}
```
1. Compile the code
```
    javac -cp SimpleAstronomyLib-0.1.0.jar WerewolfCheck.java 
```
1. Run the example
```
    java -cp .:SimpleAstronomyLib-0.1.0.jar WerewolfCheck 
```
The output from the above example says the next full moon after June 12, 2011 will be June 15 at 15:25 CDT.

# A Note on Accuracy #

Currently, the accuracy is within 15 minutes when compared to a page of [moon phases at NASA](http://eclipse.gsfc.nasa.gov/phase/phase2001gmt.html) so it's not too far off.  Originally I was looking for accuracy within 6 hours, but will be attempting to improve this. The source I used for calculations isn't the cause since the book I use has more accurate examples. Thus, I'm sure it's due to my implementation.
