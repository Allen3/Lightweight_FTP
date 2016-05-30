Development Log
===============

##30rd, May, 2016

  * `LIST` function implemented but not sound, as printed information from `PC` channel and `DTP` channel will compete
   for the `printf` resources, a mutex maybe added to solve the problem.