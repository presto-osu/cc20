This directory contains the code for the static analysis,

## Structure

- `common` contains the common utility classes and the Matlab code
- `gator` contains the code for the analysis to find "<=" pairs
- `le-pairs-csv` contains the "<=" pairs
- `randomized-response*` contain the code for the randomization

## Run

Clone the repository:

```bash
$ git clone https://github.com/presto-osu/cc20.git
$ cd cc20
```

To run the analysis, download and extract [apks.tar.gz](https://github.com/presto-osu/cc20/releases/tag/dataset) to `cc20/apks` and execute the commands:

```bash
$ cd code
$ bash runall_gator.sh
$ python process_le_pairs.py
```

To run the randomization, download and extract [dataset.tar.gz](https://github.com/presto-osu/cc20/releases/tag/dataset) to `cc20/dataset` and execute the following commands:

```bash
$ mkdir log
$ bash runall_experiments.sh
```

The log files are stored in folder `log`.
