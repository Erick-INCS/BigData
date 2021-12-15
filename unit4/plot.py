#!/usr/bin/env python3

from pandas import read_csv
from seaborn import displot

displot(
    data=read_csv('data/all_models.csv'),
    x='Time',
    y='Accuracy',
    col='Model',
    hue='Run'
).savefig('plots/models_comparation.jpg')