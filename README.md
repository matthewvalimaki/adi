# adi - Android DI
adi demonstrates simple JSON based Android specific DI.

adi is *not*:
  - complete
  - stable
  - memory efficient
  - feature rich

## Usage
1) Include DI.java to your project.
2) Add res/raw/services.json, see below how to fill the file.
3) In your main activity or class that exists through the lifetime of the application, instantiate the DI:
```java
DI di = new DI(R.raw.services, getApplicationContext());
```
4) Get service:
```java
 Picture picture = di.getService("Picture");
 picture.yourMethod();
 etc.
 ````
 
## services.json
You can rename this file to anything, just remember to update R.raw.services accordingly.

Currently the following JSON format works:
```json
{
    "NameOfService": {
        "fqns": "com.whatever.YourClass",
        "dependencies": [
            {
              "service": "YourSecondService"
            }
        ]
    },
    "YourSecondService": {
        "fqns": "com.whatever.YourSecondClass",
        "dependencies": [
            {
                "from": "context",
                "method": "getPackageManager"
            }
        ]
    }
}
```
The from and method allows explicit call to an existing Andoird application context, which we defined in Step 3 above.

The dependencies will be injected to the methods by using method consturcts. As of now there is no good logic in place to handle errors regarding this approach.