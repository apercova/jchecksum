# jchecksum

## Java cli utility for calculating hashes using java's MessageDigest class

> Uses QuickCLI to leverage cli command configuration. Check it out [here](https://github.com/apercova/QuickCLI).

#### Usage: 
```bash
  $jchecksum -help 
  
  [ckecksum]: Calculates checksum from file/text-caption
     option         aliases                   usage
     ----------     ---------------           -------------------------
     -a             [-algorithm]              Hashing algorithm
     -e             [-encoding]               Output encoding. options: [HEX,B64], default: HEX
     -f             [-file]                   File Path
     -t             [-text]                   Text caption
     -cs            [-charset]                Encoding charset
     -la                                      List available algorithms
     -lc                                      List available charsets
     -h             [-help]                   List available options
     -m             [-match]                  Compares suplied pattern against checksum

```
