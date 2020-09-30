set terminal svg size 800,800
set output '3d-polar.svg'
set border 4095 front linetype -1 linewidth 1.000
set samples 25, 25
set isosamples 40, 40
set title "Reservoir Title"
set xlabel "Test number"
set xrange [ 0.0000 : 4 ] noreverse nowriteback
set ylabel "Mutant number"
set yrange [ 0.0000 : 555 ] noreverse nowriteback
set zrange [ 0.00000 : 108130.0 ] noreverse nowriteback
set pm3d implicit at s
set pm3d interpolate 0,0
splot 'heatmap_dataset.txt' matrix with points pointtype 0

