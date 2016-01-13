# Various functions used to convert Euler angles into other parameterizations
# Work with Sage 6.10

#--- Rotations around individual axises
def rotx( index ):
    rotX = matrix(SR,3,3)
    rotX[0,0] = 1
    rotX[1,1] =  var('c{:d}'.format(index))
    rotX[1,2] = -var('s{:d}'.format(index))
    rotX[2,1] =  var('s{:d}'.format(index))
    rotX[2,2] =  var('c{:d}'.format(index))
    return rotX

def roty( index ):
    rotY = matrix(SR,3,3)
    rotY[0,0] =  var('c{:d}'.format(index))
    rotY[0,2] =  var('s{:d}'.format(index))
    rotY[1,1] =  1
    rotY[2,0] = -var('s{:d}'.format(index))
    rotY[2,2] =  var('c{:d}'.format(index))
    return rotY

def rotz( index ):
    rotZ = matrix(SR,3,3)
    rotZ[0,0] =  var('c{:d}'.format(index))
    rotZ[0,1] = -var('s{:d}'.format(index))
    rotZ[1,0] =  var('s{:d}'.format(index))
    rotZ[1,1] =  var('c{:d}'.format(index))
    rotZ[2,2] =  1
    return rotZ

def print_one( letter , index ):
    if letter == 'Z':
        return rotz(index)
    elif letter == 'Y':
        return roty(index)
    elif letter == 'X':
        return rotx(index)
    else:
        raise RuntimeError("unknown letter "+str(letter))

def print_matrix( order ):
    M0 = print_one(order[0],0)
    M1 = print_one(order[1],1)
    M2 = print_one(order[2],2)
    print "---------------   "+order
    print (M2*M1*M0)
    print

# Situation when the second rotation is 0 or pi/2
def print_pathological( order ):
    M0 = print_one(order[0],0)
    M1 = print_one(order[1],1)
    M2 = print_one(order[2],2)
    print "---------------   "+order
    A = M2*M1*M0
    print A.substitute(c1=0,s1=1,c2=1,s2=0)
    print

def symQuat( letter ):
    return [var('{}{}'.format(letter,x)) for x in range(4)]

def multQuat( a , b ):
    """
    Multiplies two quaternion arrays together and returns the result
    """
    w = a[0]*b[0] - a[1]*b[1] - a[2]*b[2] - a[3]*b[3]
    x = a[0]*b[1] + a[1]*b[0] + a[2]*b[3] - a[3]*b[2]
    y = a[0]*b[2] - a[1]*b[3] + a[2]*b[0] + a[3]*b[1]
    z = a[0]*b[3] + a[1]*b[2] - a[2]*b[1] + a[3]*b[0]
    return [w,x,y,z]

def quatAroundAxis( letter , axis ):
    """
    Creates a quaternion for rotation around the specified axis.
    @param letter = Letter in the symbol
    @param axis = (X,Y,Z) which axis it is rotated around
    """
    c = var("c{}".format(letter))
    s = var("s{}".format(letter))

    if axis == 'X':
        return [c,s,0,0]
    elif axis == 'Y':
        return [c,0,s,0]
    elif axis == 'Z':
        return [c,0,0,s]
    else:
        raise runtime_error("Crap")

def printEulerToQuat( order ):
    print order+" to quaternion"
    q0 = quatAroundAxis('a',order[0])
    q1 = quatAroundAxis('b',order[1])
    q2 = quatAroundAxis('c',order[2])
    a = multQuat(q1,q0)
    b = multQuat(q2,a)
    print "w = "+str(b[0])+";"
    print "x = "+str(b[1])+";"
    print "y = "+str(b[2])+";"
    print "z = "+str(b[3])+";"
    print


printEulerToQuat("ZYX")
printEulerToQuat("ZXY")
printEulerToQuat("YXZ")
printEulerToQuat("YZX")
printEulerToQuat("XYZ")
printEulerToQuat("XZY")
printEulerToQuat("ZYZ")
printEulerToQuat("ZXZ")
printEulerToQuat("YXY")
printEulerToQuat("YZY")
printEulerToQuat("XYX")
printEulerToQuat("XZX")

print
print "-------------------------------- Euler to Rotation Matrix"
print
print_matrix("ZYX")
print_matrix("ZXY")
print_matrix("YXZ")
print_matrix("YZX")
print_matrix("XYZ")
print_matrix("XZY")
print_matrix("ZYZ")
print_matrix("ZXZ")
print_matrix("YXY")
print_matrix("YZY")
print_matrix("XYX")
print_matrix("XZX")
print
print
print_pathological('ZYX')
print_pathological("ZXY")
print_pathological("YXZ")
print_pathological("YZX")
print_pathological("XYZ")
print_pathological("XZY")
print_pathological("ZYZ")
print_pathological("ZXZ")
print_pathological("YXY")
print_pathological("YZY")
print_pathological("XYX")
print_pathological("XZX")
print
print