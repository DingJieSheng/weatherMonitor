#coding=utf-8
import numpy as np
def nonlin(x,deriv=False):
    if(deriv==True):
        return x*(1-x)
    return 1/(1+np.exp(-x))
X = np.array([[-5,1.031,46,4,1],
[-7,1.028,49,3,1],
[-2,1.024,37,2,3],
[-3,1.027,46,2,2]])
y = np.array([[0.149],
[0.194],
[0.242],
[0.157]])
z =np.array([[0,1.026,28,7,3],
             [-1,1.027,26,4,3],
             [-2,1.028,26,2,1],
             [-2,1.030,66,2,1]])
np.random.seed(1)
# randomly initialize our weights with mean 0
syn0 = 2*np.random.random((5,15)) - 1
syn1 = 2*np.random.random((15,5)) - 1
syn2 = 2*np.random.random((5,1)) - 1
for j in range(600000):
# Feed forward through layers 0, 1, and 2
    l0 = X
    l1 = nonlin(np.dot(l0,syn0))
    l2 = nonlin(np.dot(l1,syn1))
    l3 = nonlin(np.dot(l2,syn2))
# how much did we miss the target value?
    l3_error = y - l3
#     if (np.mean(np.abs(l2_error))<0.0000001):
#         break;
    if (j% 10000) == 0:
        print("Error:" + str(np.mean(np.abs(l3_error))))
# in what direction is the target value?
# were we really sure? if so, don't change too much.
    l3_delta = l3_error*nonlin(l3,deriv=True)
    l2_error = l3_delta.dot(syn2.T)
    l2_delta = l2_error*nonlin(l2,deriv=True)
# how much did each l1 value contribute to the l2 error (according to the weights)?
    l1_error = l2_delta.dot(syn1.T)
# in what direction is the target l1?
# were we really sure? if so, don't change too much.
    l1_delta = l1_error * nonlin(l1,deriv=True)
    syn2 += l2.T.dot(l3_delta)
    syn1 += l1.T.dot(l2_delta)
    syn0 += l0.T.dot(l1_delta)
print (l3)
print (nonlin(np.dot(syn0,np.dot(syn1,syn2))))
a1=nonlin(np.dot(z,syn0))
a2=nonlin(np.dot(a1,syn1))
a3=nonlin(np.dot(a2,syn2))
print (a3)
