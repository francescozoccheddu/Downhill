import c4d
import os

from c4d import gui
from c4d import documents
from c4d import utils

separator = "="
prefix = "ui"
suffix = "Q"


def main():
    doc = documents.GetActiveDocument()
    objs = doc.GetObjects()
    old_names = []
    new_names = []
    i = 0
    for obj in objs:
        n = obj.GetName()
        w = (obj.GetRad().x + obj.GetMp().x) / 100
        old_names.append(n)
        new_names.append(n + separator + str(w))
        obj.SetName(prefix + str(i) + suffix)
        i += 1
    c4d.EventAdd()
    path = c4d.storage.SaveDialog(c4d.FILESELECTTYPE_ANYTHING, "UIExporter", "", doc.GetDocumentPath())
    c4d.documents.SaveDocument(doc, path, c4d.SAVEDOCUMENTFLAGS_DIALOGSALLOWED, c4d.FORMAT_OBJEXPORT)
    i = 0
    for obj in objs:
        obj.SetName(old_names[i])
        i += 1
    c4d.EventAdd()
    f = open(path,'r')
    filedata = f.read()
    f.close()
    for c in range(0,i):
        filedata = filedata.replace(prefix + str(c) + suffix, new_names[c])
    f = open(path,'w')
    f.write(filedata)
    f.close()
    gui.MessageDialog(str(i) + " meshes have been succesfully exported")

if __name__=='__main__':
    main()