import sys


def extract_libraries_list(meta: [str], license: str, output) -> None:
    for i in meta:
        is_full_text = False
        offsets, name = i.split(maxsplit=1)
        start, length = offsets.split(':')
        start = int(start)
        length = int(length)
        output.write('### Copyright notice for %s:\n\n' % name)
        if license[start:start+4] != 'http':
            if license[start:start+3] == 'ttp':
                output.write('h')
            else:
                is_full_text = True
                output.write('```\n')
        output.write(license[start:start+length])
        if is_full_text:
            output.write('```')
        output.write('\n\n')
    output.close()


if __name__ == "__main__":
    ROOT_DIR = './app/build/generated/third_party_licenses/res/raw/'
    with open('%sthird_party_license_metadata' % ROOT_DIR, 'rt') as f:
        meta = f.read()
    meta = meta.splitlines()
    with open('%sthird_party_licenses' % ROOT_DIR, 'rt') as f:
        license = f.read()
    with open(sys.argv[1], 'wt') as output:
        extract_libraries_list(meta, license, output)
