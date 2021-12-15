#!/usr/bin/env python3

import pandas as pd
from pathlib import Path

dirs = Path('data/times').glob('*')

final_csv = pd.DataFrame(columns=['Model', 'Run','Time','Accuracy'])

for directory in dirs:
    files = map(
        lambda file_path: pd.read_csv(file_path),
        directory.glob('*.csv')
    )

    csv = pd.concat(files, ignore_index=True)
    csv.to_csv(
        directory.parent.joinpath(f'{directory.name}.csv'),
        index=False)
    
    csv['Model'] = directory.name
    final_csv = final_csv.append(csv, ignore_index=True)
    final_csv.to_csv('data/all_models.csv', index=False)
