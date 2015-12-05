package com.basho.riak.client.core.query.timeseries;

import com.basho.riak.protobuf.RiakTsPB;
import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Alex Moore <amoore at basho dot com>
 * @author Sergey Galkin <srggal at gmail dot com>
 * @since 2.0.3
 */
public final class CollectionConverters
{
    private CollectionConverters() {}

    public static List<RiakTsPB.TsRow> convertRowsToPb(Collection<Row> rows)
    {
        ArrayList<RiakTsPB.TsRow> tsRows = new ArrayList<RiakTsPB.TsRow>(rows.size());
        for (Row row : rows)
        {
            tsRows.add(row.getPbRow());
        }
        return tsRows;
    }

    public static List<RiakTsPB.TsCell> convertCellsToPb(Collection<Cell> cells)
    {
        ArrayList<RiakTsPB.TsCell> tsCells = new ArrayList<RiakTsPB.TsCell>(cells.size());
        for (Cell cell : cells)
        {
            if(cell == null)
            {
                tsCells.add(Cell.NullCell.getPbCell());
            }
            else
            {
                tsCells.add(cell.getPbCell());
            }
        }
        return tsCells;
    }

    public static Collection<RiakTsPB.TsColumnDescription> convertColumnDescriptionsToPb
            (Collection<ColumnDescription> columns)
    {
        final ArrayList<RiakTsPB.TsColumnDescription> pbColumns =
                new ArrayList<RiakTsPB.TsColumnDescription>(columns.size());

        for (ColumnDescription column : columns)
        {
            pbColumns.add(convertColumnDescriptionToPb(column));
        }

        return pbColumns;
    }

    private static RiakTsPB.TsColumnDescription convertColumnDescriptionToPb(ColumnDescription column)
    {
        final RiakTsPB.TsColumnDescription.Builder columnBuilder = RiakTsPB.TsColumnDescription.newBuilder();
        columnBuilder.setName(ByteString.copyFromUtf8(column.getName()));

        columnBuilder.setType(RiakTsPB.TsColumnType.valueOf(column.getType().ordinal()));

        return columnBuilder.build();
    }

    public static List<ColumnDescription> convertPBColumnDescriptions(List<RiakTsPB.TsColumnDescription> pbColumns)
    {
        if (pbColumns == null)
        {
            return Collections.emptyList();
        }

        final ArrayList<ColumnDescription> columns = new ArrayList<ColumnDescription>(pbColumns.size());

        for (RiakTsPB.TsColumnDescription pbColumn : pbColumns)
        {
            ColumnDescription columnDescription = convertPBColumnDescription(pbColumn);
            columns.add(columnDescription);
        }

        return columns;
    }

    private static ColumnDescription convertPBColumnDescription(RiakTsPB.TsColumnDescription pbColumn)
    {
        final String name = pbColumn.getName().toStringUtf8();

        final ColumnDescription.ColumnType type = ColumnDescription.ColumnType.values()[pbColumn.getType().getNumber()];

        return new ColumnDescription(name, type);
    }
}
