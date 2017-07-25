#coding=utf-8
import numpy as np
def nonlin(x,deriv=False):
    if(deriv==True):
        return x*(1-x)
    return 1/(1+np.exp(-x))
X = np.loadtxt("C:\\Users\\ac\\Desktop\\exportdata1.txt")#正式部署时应该改为exportdata.txt

y = np.loadtxt("C:\\Users\\ac\\Desktop\\aimdata1.txt")#正式部署时应该改为aimdata.txt
y = y.reshape(y.size,1)
np.random.seed(1)
# randomly initialize our weights with mean 0
syn0 = 2*np.random.random((4,15)) - 1
syn1 = 2*np.random.random((15,5)) - 1
syn2 = 2*np.random.random((5,1)) - 1
for i in range((int)(X.size/4)-2):
    X1=X[i:i+3]
    y1=y[i:i+3]
    for j in range(600000):
    # Feed forward through layers 0, 1, and 2
        l0 = X1
        l1 = nonlin(np.dot(l0,syn0))
        l2 = nonlin(np.dot(l1,syn1))
        l3 = nonlin(np.dot(l2,syn2))
    # how much did we miss the target value?
        l3_error = y1 - l3
    #     if (np.mean(np.abs(l2_error))<0.0000001):
    #         break;
#         if (j% 10000) == 0:
#             print("Error:" + str(np.mean(np.abs(l3_error))))
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
syn0=syn0.reshape(4,15)
syn1=syn1.reshape(15,5)
syn2=syn2.reshape(5,1)
np.savetxt("weight0.txt",syn0,fmt="%f\r\n")
np.savetxt("weight1.txt",syn1,fmt="%f\r\n")
np.savetxt("weight2.txt",syn2,fmt="%f\r\n")
# print (Z)
# a1=nonlin(np.dot(z,syn0))
# a2=nonlin(np.dot(a1,syn1))
# a3=nonlin(np.dot(a2,syn2))
# print (a3)
