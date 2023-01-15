from scipy.spatial import ConvexHull, convex_hull_plot_2d
import numpy as np
from shapely import Point, Polygon
from rts.models import RTSMap

class RTS_Map():

    def __init__(self, db_id):
        self.db_id = db_id
        self.obstacles = []
        self.explored_bound = []
        # TODO remove
        self.explored_squares = []
        self.starting_point = (0,0)
        # size of the current grid represented by distance of border from starting point in each direction
        self.up_squares = 50
        self.down_squares = 50
        self.left_squares = 50
        self.right_squares = 50

    def update_size(up_squares, down_squares, left_squares, right_squares):
        self.left_squares = left_squares
        self.up_squares = up_squares
        self.down_squares = down_squares
        self.right_squares = right_squares

    def add_point(self, x : int, y : int, obstacle : bool):
        if obstacle and (x,y) not in self.obstacles:
            self.obstacles.append((x, y))
        if (x,y) not in self.explored_squares:
            self.explored_bound.append((x, y))
            self.explored_squares.append((x, y))
        # TODO don't do this every time
        #self.update_bounds()        
    
    def save_to_db(self):
        map_in_db = RTSMap.objects.get(pk=self.db_id)
        if map_in_db is None:
            raise Exception("can't save map, could not find map in DB")
        # TODO non evil version
        # merge the two maps
        map_in_db.obstacles = map_in_db.obstacles + (set(self.obstacles) - set(map_in_db.obstacles))
        map_in_db.explored_fields = map_in_db.explored_fields + (set(self.explored_squares) - set(map_in_db.explored_fields))
        map_in_db.save()


    def update_bounds(self):
        hull = ConvexHull(self.explored_bound)
        self.explored_bound = [self.explored_bound[i] for i in hull]

    def to_list(self, include_visited : bool = False):
        # create the array initialized with zeroes        
        ret = np.zeros((self.up_squares + self.down_squares, self.left_squares + self.right_squares))
        if include_visited == False:
            for el in self.obstacles:                
                x = el[0]
                y = el[1]
                ret[x][y] = 1                
        else:
            for el in self.explored_squares:
                x = el[0]
                y = el[1]
                ret[x,y] = 2
        return ret

            
