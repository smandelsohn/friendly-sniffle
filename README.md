# AccuServices

Service daemon for interfacing with the AccuZip and AccuConnect services.

## Notes

  * Based on spring integration framework, just simple ESB. Close to CAMEL esb
  * No need to have repositories, entities, etc. Just wire sql , schedules, rest and beans in one app
  * ATM entities are used to create testing DB only
  * All DB updates, which are necessary - stored as part of eprint to keep changes in one place
   
## Process notes

### Accu Zip

This process looks for records in **NEW** state in PRINTREQUEST table and pick up such records for processing. Records with null or empty state are not picked up, because olb records may be taking into processing.

For **NEW** print request application will create CSV file with addresses  for accu zip, upload it and change status to **CSVUP**.

The cass and deduplication jobs will be requested for uploaded files, in case of success the **CSVUP** status will be changed to **REQ**

AccuZip service will be asked if requested jobs are ready, if so status will be updated from **REQ** to **READY** And print request acc_count field in PRINTREQUEST table will have quantity of valid addresses. 

Downloading of valid addresses will be performed for order in complete status, After that the **READY** status will be changed to **DONE**

### Accu Connect


Accu Connect part of application pickup records with verified addresses (in DONE state) and compose json request to external service.
    
The call result will set final status of print request record to **PRINTOK** or  **PRINTFAIL** immediately after call. App has no ability to track print and send statuses.

#### Callback

After submitting jobs to AccuConnect they will be processed in batches. When AccuConnect finishes processing, a request will be posted to the provided callback url with details of the processed jobs. Those jobs will then be set to PROCESSED status.
  
## Order status happy day flow

  **NEW** -> **CHECKUP** -> **PRECSVUP** -> **CSVUP** -> **REQ** -> **READY** -> **DONE** -> **PRINTOK** -> **PROCESSED** 
  
 * NEW - new order with print request in NEW state 
* CHECKUP Check upload
* PRECSVUP addresses are collected and uploaded to accu zip 
* CSVUP Update quote is called 
* REQ requested cass and dedup jobs for uploaded addressed 
* READ dedup addresses available to download . Also we know quantity of verified addresses and wait for order to be paid. After order complete we can download the list of addresses
* DONE verified list of addresses is downloaded and placed into db 
* PRINTOK or **PRINTFAIL** states, means that the order for accu connect is created and sent. The success answer from accu connect lead to PRINTOK status, otherwise PRINTFAIL.
* PROCESSED after AccuConnect processes a batch and submits a POST request to the callback url.

## Error flow and error handling.

 * The accu zip can return zero verified addresses. Need to mark print request with error state. So print request will be marked as **ERRNOADDR** 
 * We dont have PDFs (whatever from chiliUserPrintProduction table) for print request. Need to mark print request with error state.  Marked as **ERRNOPDF**

In **ERRXXXXXX** status orders not picked up for processing. So additional end point may required to change the status after error fixing. 

## TLS support (not yet)

 * Support **PKCS12** format, not java specific **JKS** 
 * Add file to resource and specify it in **server.ssl.key-store**  key . Example **server.ssl.key-store=classpath:corelogic.p12** or **server.ssl.key-store=/some/path/to-file.p12**  
 
## Accu Zip & Accu Connect doc

Attached to CWH-4985 and CWH-4937 issues.  
Bump
# friendly-sniffle
# friendly-sniffle
