# James McShane Search Implementation

[![Build Status](https://travis-ci.org/jmcshane/search.svg?branch=master)](https://travis-ci.org/jmcshane/search)

This project carries out three search mechanisms:

* Full text document search
* Regular expression document search
* Search an index containing preprocessed content

## Code Organization

The high-level organization of the code is done using gradle subprojects.  There are three modules under search, each implementing the `SearchStrategy` class present in the `api` module.  The `boot` module starts up the app and makes HTTP GET endpoints available for each search mechanism.  The web endpoints choose the appropriate `SearchStrategy` implementation and forward requests to the submodule.

Though each module individually 