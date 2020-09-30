set pm3d map
set terminal svg size 800,800
set output '2d-kill.svg'
set palette model RGBset palette model RGB defined (0 "green", 2 "dark-red" )splot 'heatmap_dataset.txt' matrix

