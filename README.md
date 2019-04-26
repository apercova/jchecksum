# jchecksum  
[![Build Status](https://travis-ci.org/apercova/jchecksum.svg?branch=master)](https://travis-ci.org/apercova/jchecksum)
## Java CLI utility for calculating hashes using java's MessageDigest class

> Uses QuickCLI to leverage CLI command configuration. Check it out [here](https://github.com/apercova/QuickCLI).

### Installation:
#### 1. Clone repository
```bash
$ git clone https://github.com/apercova/jchecksum.git
Cloning into 'jchecksum'...
remote: Enumerating objects: 116, done.
remote: Counting objects: 100% (116/116), done.
remote: Compressing objects: 100% (52/52), done.
remote: Total 227 (delta 26), reused 83 (delta 11), pack-reused 111
Receiving objects: 100% (227/227), 33.55 KiB | 981.00 KiB/s, done.
Resolving deltas: 100% (54/54), done.
```

#### 2. Package with maven 
> Installation at ~/.m2 local maven repository is not needed.
```bash
$ cd jchecksum/
$ mvn clean package
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building jchecksum 1.0.1904
[INFO] ------------------------------------------------------------------------
... [more log content] ...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 17.487 s
[INFO] Finished at: 2019-04-25T18:35:30-05:00
[INFO] Final Memory: 27M/195M
[INFO] ------------------------------------------------------------------------
```

#### 3. Identify different installation artifacts located inside ```dist``` directory:
- Javadoc jar: ```jchecksum-1.0.1904-javadoc.jar```
- Sources jar: ```jchecksum-1.0.1904-sources.jar```
- Executable jar: ```jchecksum-1.0.1904.jar```
- Windows batch init script: ```jchecksum.cmd```
- Linux bash init script: ```jchecksum.sh```
```bash
$ ls dist/|grep jchecksum
jchecksum-1.0.1904-javadoc.jar
jchecksum-1.0.1904-sources.jar
jchecksum-1.0.1904.jar
jchecksum.cmd
jchecksum.sh
```

- Library dir: ```/lib``` with dependencies
```bash
$ ls dist/lib/ |grep .
hamcrest-core-1.3.jar
junit-4.12.jar
quickcli-1.0.1904.jar
```

#### 4. Add ```dist``` dir to ```PATH``` enviroment variable and execute ```jchecksum``` command. Look at the usage examples below.
 - On Windows:
```bash 
set PATH=C:\path\to\dist;%PATH%
jchecksum
d41d8cd98f00b204e9800998ecf8427e
```
 - On Linux
```bash 
$ PATH=/path/to/dist:$PATH
$ jchecksum
d41d8cd98f00b204e9800998ecf8427e
```

### Usage: 
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
### Examples:
#### Calculate text hash
- Calculate hex-encoded (default encoding) md5-hash of text ```password``` (Notice quoted text)
```bash
$ jchecksum -a md5 -t "password"
$ 5f4dcc3b5aa765d61d8327deb882cf99
```
- Above code is equivalent to:
```bash
$ jchecksum -a md5 -t "password" -e hex
$ 5f4dcc3b5aa765d61d8327deb882cf99
```

#### Calculated file hash
- Calculate b64-encoded sha-256-hash of file ```/home/user/file.txt```
```bash
$ jchecksum -a sha-256 -e b64 -f /home/user/file.txt
$ uz2FKQLSUJ4G3n7A+ie8oWxjWfb5qwf3IfK2yYKobLA=
```

#### Test for matching hash against precalculated hash
> Using [-m hash] argument, command call returns ```true``` if hash matches against provided precalculated hash. ```false``` otherwise.  
- Asumming that precalculated hex-encoded md5-hash of ```/home/user/file.txt``` is: ```ebc6b76035669d8e7da4e6daf3e835e8```, test for hex-encoded md5 hash matching as follows (Notice quoted text):
```bash
$ jchecksum -a md5 -f /home/user/file.txt -m "ebc6b76035669d8e7da4e6daf3e835e8"
$ true
```
- Asumming that precalculated b64-encoded sha1-hash of text ```password``` is: ```W6ph5Mm5Pz8GgiULbPgzG37mj9g=```, test for b64-encoded sha1 hash matching as follows (Notice quoted text):
```bash
$ jchecksum -a sha1 -e b64 -t "password" -m "W6ph5Mm5Pz8GgiULbPgzG37mj9g="
$ true
```

### Encode-only source value
> jchecksum can be told to just encode source value both from file or inline-text.  
- Encode inline text as follows:
```bash
$ jchecksum -t "clear text to encode" -e b64 -eo
$ Y2xlYXIgdGV4dCB0byBlbmNvZGU=
```
- Encode file ```/home/user/file.txt``` to Base64 as follows:
```bash
$ jchecksum -f /home/user/file.txt -e b64 -eo
$ TAAAAAEUAgAAAAAAwAAAAAAAAEabAAgAIAAAAHobqFSvDNQBehuoVK8M1AF6i9HOrwzUAT ...
```

### Special java SerialVersionUID encoding
> jchecksum can be told to generate java SerialVersionUID Long value just providing ```juid|JUID``` special encoding format as encoding argument.  
> It works well in both hashing mode and encode-only mode but since hashing value is almost unique, it's recommended to use this mode.  
- Encode-only mode:
```bash
$ jchecksum -e juid -eo -f /home/user/MyClass.java 
$ 6250996317149986816
```
- Md5-hashing mode:
```bash
$ jchecksum -a md5 -e juid -f /home/user/MyClass.java
$ 9053614978990880232
```
- Sha-512-hashing mode:
```bash
$ jchecksum -a sha-512 -e juid -f /home/user/MyClass.java
$ 1214078597467355053
```
