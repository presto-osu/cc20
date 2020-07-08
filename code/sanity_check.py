#!/usr/bin/env python3
import json
import csv
import glob

for csvf in glob.glob('./le-pairs-csv/*.csv'):
    app = csvf.split('/')[-1][:-len('.csv')]
    # if app != 'vidanta':
    #     continue
    print(app)
    jsonf = '../dataset/%s.json' % app

    with open(jsonf) as f:
        mtds = json.load(f)
    mtds = [e['sig'] for e in mtds]
    mtds_in_pairs = set()
    with open(csvf, newline='') as f:
        reader = csv.reader(f, delimiter='\t', quotechar=' ')
        for row in reader:
            for m in row:
                mtds_in_pairs.add(m.replace('  ', ' '))

    # print(mtds_in_pairs)
    for m in mtds_in_pairs:
        if m not in mtds:
            print(m)
