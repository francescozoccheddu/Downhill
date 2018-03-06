import c4d
import os
from c4d import gui

if __name__ == "__main__":
    doc = c4d.documents.GetActiveDocument()
    objs = doc.GetObjects()
    out = "";
    spline_count = 0;
    if len(objs) > 0: 
        for ob in objs:
            if ob.GetTypeName() == "Spline":
                point_count = ob.GetPointCount()
                out += "S " + ob.GetName() + " " + str(point_count) + "\n"
                if point_count > 8:
                      gui.MessageDialog(ob.GetName() + ' has more than 8 vertices')
                spline_count += 1
                for i in range(0, point_count):
                    point = ob.GetPoint(i)
                    out += "V " + str(point.x / 100) + " " + str(point.y / 100) + "\n"
    if spline_count > 0:
        path = c4d.storage.SaveDialog(c4d.FILESELECTTYPE_ANYTHING, "SplineExporter", "", doc.GetDocumentPath())
        if path and (os.path.exists(path) or os.access(os.path.dirname(path), os.W_OK)):
            text_file = open(path, "w")
            text_file.write(out)
            text_file.close()
            gui.MessageDialog(str(spline_count) + ' splines have been succesfully exported')
        else:
            gui.MessageDialog('Could not write file')
    else:
        gui.MessageDialog("There aren't splines in scene")