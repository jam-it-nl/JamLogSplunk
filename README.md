# JamLogSplunk
Centralize the logging of all your Mendix projects in Splunk.

Easy to implement, this module does not require any change in the Mendix module. Adding the module and configuring the constants is all that is needed.

# Installation
* Import Module
* Make sure AfterStartup is called after startup

# Configuration
After creating an account on https://www.splunk.com you should open Splunk Cloud and check whether the HTTP Event Collector is enabled. (It is enabled by default on Splunk Cloud). Create an Event Collector token on Splunk Cloud. More information can be found on Splunk.

**Set constants:**

* EnableOnLocalhost: Set this constant to false if you do not want to sent log from localhost to the external system.
* SplunkLogLevel: The level on which the log be logged in Splunk. The possible values are TRACE, DEBUG, INFO, WARNING, ERROR, CRITICAL, NONE.
* SplunkToken: Token used to authenticate to Splunk. You can find the token value in the HTTP Event Collector on Splunk Cloud.
* SplunkUrl: Endpoint of the Splunk HTTP Event Collector. You can be found the url in the address bar of Splunk Coud. The standard port number 8088 is used. For example: https://inputs.prd-p-XXXX.splunkcloud.com:808

You can find the log by navigate to 'Search & Reporting' from Splunk Cloud home page.
In the search bar, you can search for 'sourcetype="httpevent"' or 'source = http:{NameOfTheToken}' to find the desired log.
Details of the log can be find by click the '+' icon to expand the message. You also have a option to 'Show as raw text' for each event.
