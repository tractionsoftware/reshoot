reshoot
=======

Reshoot uses Selenium WebDriver to automate shooting product screenshots.

We created this tool to make it easy to keep our product screenshots up-to-date with the latest version. A JSON configuration file specifies the URLs and crop dimensions of the screenshots.

## Example

Create a configuration file `screenshots.json`:

```json
{
    "screenshots": [
        {
            "url": "http://google.com",
            "output": "target/google.png",
            "browser": { "width": 980 },
            "crop": { "left": 40, "top": 50, "width": 900, "height": 450 }
        }
    ]
}
```

Run reshoot with the configuration file:

```
java -jar Reshoot-1.1.jar screenshots.json
```

## Usage

```
Reshoot uses Selenium WebDriver to automate shooting product screenshots

  Usage:
    java -jar Reshoot.jar [options] [files...]

  Options:
    --chrome   use Chrome (for retina screenshots on retina displays)
    --firefox  use Firefox (for full scrollheight screenshots)

  Documentation:
    https://github.com/tractionsoftware/reshoot
```

## Setups

Setups allow you to specify a Java class to execute some WebDriver commands before and after taking the screenshot. This can be useful for logging in, opening dialogs, filling out forms, etc.

The example [GoogleQuery](src/main/java/com/tractionsoftware/reshoot/example/GoogleQuery.java) shows how to add a query to Google before taking a screenshot of the results:

```json
{
        "browser": { "width": 980 },
        "setups": {
                "google": {
                        "class": "com.tractionsoftware.reshoot.example.GoogleQuery",
                        "query": "webdriver"
                }
        },
        "screenshots": [
                {
                        "url": "http://google.com",
                        "output": "target/google.png",
                        "browser": { "width": 980 },
                        "crop": { "left": 110, "top": 0, "width": 680, "height": 450 },
                        "setup": "google"
                }
        ]
}
```

This is very much a work-in-progress. 
