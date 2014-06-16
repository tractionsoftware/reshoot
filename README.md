reshoot
=======

Reshoot uses Selenium WebDriver to automate shooting product screenshots.

We created this tool to make it easy to keep our product screenshots up-to-date with the latest version. A JSON configuration file specifies the URLs and crop dimensions of the screenshots.

Example:

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

This is very much a work-in-progress. 
