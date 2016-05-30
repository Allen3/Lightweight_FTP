Development Log
===============


##13rd, May, 2016
   
  * Adjust the `LIST` response data format. 
   
  * Add `DELE` function. (This request never fails even when the file is opened, perhaps it casts a pending I/O 
  operation to be processed after current one is done.)

  * Add `FEAT` function with only `UTF8` in the feature list.

TODO:

  * Handle Exception occurred in DTP channel that client close the data transfer without any command request.
  
  * The `encoding` problem still exists even after adjusting the `LIST`  response data format.

  
##21st, May, 2016
  
  * A bug detected by using linux virtualbox node to request `E:\\photos\malaysia\IMG_1830.jpg`, which caused a 
   exception.
   
  * Binary file transfer still has a deadly flaw for the file transferred doesn't match perfectly to the original one
  as a test file of `google-chrome-stable_current_amd64.deb`.
  
##24th, May, 2016

  * Bug 2 of `21st, May, 2016` resolved by setting client data transferring mode to `binary`.
  
  * Bug 1 of `21st, May, 2016` resolved by Modifying `PortCmdHandler` while, adding `isCanceled()` to the validation of
  the `stateContext.getChannelSyncEvent()`.
  
  * `Encoding` problem resolved by accident(It just works! ORZ... ). IMHO, it may be credited to the implementation of 
  `FEAT` or the `LIST` format adjustment.
  
##30, May, 2016
  
  * Modify the `Res_257`.