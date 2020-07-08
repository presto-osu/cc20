#!/usr/bin/env python3

import glob
import json

mtds = {}

app_map = {
    'quicknews.json': 'com.quick.world.news.apk.json',
    'drumpads.json': 'com.paullipnyagov.hiphopdrumpads24.apk.json',
    'moonphases.json': 'simon.sander.moonphases.apk.json',
    'vidanta.json': 'com.grupovidanta.android.app.apk.json',
    'equibase.json': 'com.equibase.todaysracing.apk.json',
    'parking.json': 'il.talent.parking.apk.json',
    'speedlogic.json': 'com.speedlogicapp.speedlogiclite.apk.json',
    'barometer.json': 'net.hubalek.android.apps.barometer.apk.json',
    'localtv.json': 'com.goforit.localtv.apk.json',
    'bible.json': 'com.daily.bible.verse.app.apk.json',
    'loctracker.json': 'com.phonelocationtracker.track.apk.json',
    'mitula.json': 'com.mitula.homes.apk.json',
    'dpm.json': 'com.agminstruments.drumpadmachine.apk.json',
    'post.json': 'com.reto.post.egydream.apk.json',
    'parrot.json': 'com.SearingMedia.Parrot.apk.json',
}

inv_app_map = {v: k for k, v in app_map.items()}


def read(dir):
    for fname in glob.glob(dir):
        app = fname.split('/')[-1]
        # print(app, end='')
        mtds[app] = set()
        with open(fname, 'r') as f:
            j = json.load(f)
            for k in j:
                mtds[app].add(k['sig'])
        # print('\t%d' % len(mtds[app]))


def process(dir, symbol):
    ret = {}
    for fname in glob.glob(dir):
        app = fname.split('/')[-1]
        app = inv_app_map[app]
        # print(app, end='')
        ret[app] = set()
        with open(fname, 'r') as f:
            j = json.load(f)
            for k in j:
                parts = k.split(symbol)
                l = parts[0]
                r = parts[1]
                if l in mtds[app] and r in mtds[app]:
                    ret[app].add((l, r))
        # print('\t%d' % len(ret[app]))
    return ret


if __name__ == '__main__':
    read('../dataset/*.json')
    le_pairs = process('cg-le/*.json', ' <<< ')
    ge_pairs = process('cg-ge/*.json', ' >>> ')
    for app in le_pairs:
        print(app, end='')
        counter = 0
        with open('./le-pairs-csv/%s.csv' % app[:-len('.json')], 'w') as f:
            for p in le_pairs[app]:
                f.write(p[0].replace(' ', '  ') + '\t' + p[1].replace(' ', '  ') + '\n')
                counter += 1
            for p in ge_pairs[app]:
                f.write(p[1].replace(' ', '  ') + '\t' + p[0].replace(' ', '  ') + '\n')
                counter += 1
        print('\t%d' % counter)
