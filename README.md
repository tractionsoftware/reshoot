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
java -jar Reshoot-1.0.jar screenshots.json
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


This is very much a work-in-progress. 
