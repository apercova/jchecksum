# jchecksum  
[ ![Codeship Status for apercova/jchecksum](https://app.codeship.com/projects/bc8527e0-728a-0136-a4f5-16905de121f2/status?branch=master)](https://app.codeship.com/projects/299445)
## Java cli utility for calculating hashes using java's MessageDigest class

> Uses QuickCLI to leverage cli command configuration. Check it out [here](https://github.com/apercova/QuickCLI).

#### Usage: 
```bash
$ jchecksum --help
[jchecksum]: Calculates checksum from file/text-caption
     option         aliases                   usage
     ----------     ---------------           -------------------------
     -a             [--algorithm]             Hashing algorithm
     -e             [--encoding]              Output encoding. options: [HEX,B64], default: HEX
     -eo            [--encode-only]           If set, source only gets encoded
     -f             [--file]                  File Path
     -t             [--text]                  Text caption
     -cs            [--charset]               Encoding charset
     -la                                      List available algorithms
     -lc                                      List available charsets
     -le                                      List available encoding options
     -h             [--help]                  List available options
     -m             [--match]                 If set, checksum result gets compared against suplied pattern
```
