reshoot
=======

Reshoot uses Selenium WebDriver to automate shooting product screenshots.

We created this tool to make it easy to keep our product screenshots up-to-date with the latest version. A JSON configuration file specifies the URLs and crop dimensions of the screenshots.

Example:

```
{
        "username": "admin",
        "password": "",
        "screenshots": [
                // logo image
                {
                        "url": "http://localhost:8000",
                        "output": "target/logo.png",
                        "browser": { "width": 980 },
                        "crop": { "left": 15, "top": 10, "width": 80, "height": 80 }
                }
        ]
}
```

Note: username/password options are only useful for Teampage login.

This is very much a work-in-progress. 
